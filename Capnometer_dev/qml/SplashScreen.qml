import QtQuick 2.5
import QtQuick.Controls 2.3
import "."

Page {
    id: root
    anchors.fill: parent

    property bool appIsReady: false
    property bool splashIsReady: false
    onSplashIsReadyChanged: if (splashIsReady) readyToGo();

//    property bool ready: appIsReady && splashIsReady
//    onReadyChanged: if (ready) readyToGo();

    signal readyToGo()

    function appReady()
    {
        appIsReady = true
    }

    function errorInLoadingApp()
    {
        Qt.quit()
    }

    Image {
        anchors.centerIn: parent
        width: Math.min(parent.height, parent.width)*0.6
        height: CapnometerSettings.heightForWidth(width, sourceSize)
        //source: "/image/splash.png"
        //source: "/image/sunbright 1036.png"
        source: "/image/neutral.png"
        //source: "/image/capnomed.jpg"
    }

    Timer {
        id: splashTimer
        interval: 4000
        onTriggered: splashIsReady = true
    }

    Component.onCompleted: splashTimer.start()
}
