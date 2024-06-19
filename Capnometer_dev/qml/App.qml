import QtQuick 2.0
import QtQuick.Controls 2.2
import QtSensors 5.0
import QtQuick.Window 2.2
import "."

Page {
    visible: true

    Component.onCompleted: {
        CapnometerSettings.wWidth = Qt.binding(function() {return width})
        CapnometerSettings.wHeight = Qt.binding(function() {return height})
    }

    Loader {
        id: splashLoader
        anchors.fill: parent
        source: "SplashScreen.qml"
        visible: true
        onStatusChanged: {
            if (status === Loader.Ready) {
                //appLoader.setSource("main.qml");
                deviceFinder.startSearch()
            }
        }
    }

    Loader {
        id: appLoader
        anchors.fill: parent
        visible: true
        asynchronous: false
        onStatusChanged: {
            //if (status === Loader.Ready)
                //splashLoader.item.appReady()
            if (status === Loader.Error)
                splashLoader.item.errorInLoadingApp();
        }
    }

    Connections {
        target: splashLoader.item
        onReadyToGo: {
            appLoader.visible = true
            appLoader.setSource("main.qml")
            splashLoader.visible = false
            splashLoader.setSource("")
            appLoader.item.forceActiveFocus();
        }
    }




}
