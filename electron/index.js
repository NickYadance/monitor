const {app, BrowserWindow} = require('electron');

let win;
let serverProcess;
let mainWindow;

function createWindow() {
    let platform = process.platform;

    if (platform === 'win32') {
        serverProcess = require('child_process')
            .spawn('cmd.exe', ['/c', 'demo.bat'],
                {
                    cwd: app.getAppPath() + '/demo/bin'
                });
    } else {
        serverProcess = require('child_process')
            .exec("java -jar ./lib/monitor-0.0.2-SNAPSHOT.jar", function (err, stdout, stderr) {
                if (err){
                    console.error("error while invoking server process", err)
                }
                console.log(stdout)
            });
    }

    if (!serverProcess) {
        console.error('Unable to start server from ' + app.getAppPath());
        app.quit();
        return;
    }

    serverProcess.stdout.on('data', function (data) {
        console.log('Server: ' + data);
    });

    console.log("Server PID: " + serverProcess.pid);

    let appUrl = 'http://localhost:2019/main';

    const openWindow = function () {
        mainWindow = new BrowserWindow({
            title: 'Bandwidth Monitor',
            width: 800,
            height: 800
        });

        mainWindow.loadURL(appUrl);

        mainWindow.on('closed', function () {
            mainWindow = null;
        });

        mainWindow.on('close', function (e) {
            if (serverProcess) {
                e.preventDefault();

                // kill Java executable
                const kill = require('tree-kill');
                kill(serverProcess.pid, 'SIGTERM', function () {
                    console.log('Server process killed');

                    serverProcess = null;

                    mainWindow.close();
                });
            }
        });
    };

    const startUp = function () {
        const requestPromise = require('minimal-request-promise');

        requestPromise.get(appUrl).then(function (response) {
            console.log('Server started!');
            openWindow();
        }, function (response) {
            // console.log('Waiting for the server start...');

            setTimeout(function () {
                startUp();
            }, 200);
        });
    };

    startUp();
}

app.on('ready', createWindow);

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
    app.quit()
}
});

app.on('activate', () => {
    if (win === null) {
    createWindow()
}
});
