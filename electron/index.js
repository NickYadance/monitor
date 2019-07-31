const {app, BrowserWindow} = require('electron');

let win;
let appUrl = 'http://localhost:2019/main';

function createWindow() {
    let platform = process.platform;

    if (platform === 'win32') {
        require('child_process')
            .spawn('cmd.exe', ['/c', 'demo.bat'],
                {
                    cwd: app.getAppPath() + '/demo/bin'
                });
    } else {
        let options = {
            name: 'Electron',
        };

        require('sudo-prompt')
            .exec("java -jar ./lib/monitor-0.0.2-SNAPSHOT.jar", options, function (err, stdout, stderr) {
                if (err){
                    console.error("error while invoking server process", err)
                }
                console.log(stdout);
                console.log(stderr);
            });
    }
    startUp();
}

const startUp = function () {
    const requestPromise = require('minimal-request-promise');

    requestPromise.get(appUrl).then(function (response) {
        console.log('Server started!');
        openWindow();
    }, function (response) {
        setTimeout(function () {
            startUp();
        }, 200);
    });
};

const openWindow = function () {
    win = new BrowserWindow({
        title: 'Bandwidth Monitor',
        width: 800,
        height: 800
    });

    win.loadURL(appUrl);

    win.on('closed', function () {
    });

    win.on('close', function (e) {
    });
};

app.on('ready', createWindow);

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }});

app.on('activate', () => {
    if (win === null) {
        createWindow()
    } else {
        openWindow()
    }});