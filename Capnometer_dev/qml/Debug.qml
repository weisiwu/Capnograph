import QtQuick 2.0
import QtQuick.Controls 2.3

Page {
    title: qsTr(language.sDebug)
    visible: true

    Loader {
        id: psLoader
        anchors.fill: parent
        source: "PasswordInput.qml"
        asynchronous: false
        visible: true

        onStatusChanged: {
            if (status === Loader.Ready) {
                appLoader.setSource("DebugPage.qml");
            }
        }
    }

    Connections {
        target: psLoader.item
        onPasswordRight: {
            appLoader.visible = true
            appLoader.item.init()
            psLoader.visible = false
            psLoader.setSource("")
            appLoader.item.forceActiveFocus();
        }
    }

    Loader {
        id: appLoader
        anchors.fill: parent
        visible: false
        asynchronous: true
    }

}
