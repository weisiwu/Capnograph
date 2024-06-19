import QtQuick 2.0
import QtQuick.Controls 2.3

Page {
    title: qsTr(language.sDebug)
    anchors.fill: parent

    property bool pwRight: false
    onPwRightChanged: if (pwRight) passwordRight();
    property bool showError: false

    signal passwordRight()

    Column{
        width: parent.width*0.9
        height: parent.height
        anchors.top:parent.top
        anchors.topMargin: CapnometerSettings.fieldHeight
        anchors.horizontalCenter: parent.horizontalCenter
        spacing: 40

        Image {
            id: lock
            width: CapnometerSettings.fieldHeight*0.5
            height: CapnometerSettings.heightForWidth(width, sourceSize)
            source: "/image/lock.png"
        }
        Label{
            id:pslable
            text: language.sPSInput
            font.pixelSize: CapnometerSettings.bigFontSize
        }
        TextField{
            id:textInput
            anchors.horizontalCenter: parent.horizontalCenter
            width: parent.width
            height: CapnometerSettings.fieldHeight
            cursorVisible: true
            echoMode:TextInput.Password
        }
        Button{
            text:language.OK
            flat: true
            anchors.horizontalCenter: parent.horizontalCenter
            width: parent.width
            height: CapnometerSettings.fieldHeight
            onClicked: {
                if(textInput.text === "123456")
                    pwRight=true
                else{
                    pwRight=false
                    showError=true
                    showTimer.running=true
                }

            }
        }
        Label{
            id:errorlable
            anchors.horizontalCenter: parent.horizontalCenter
            text: language.sPSerror
            font.pixelSize: CapnometerSettings.smallFontSize
            color:"red"
            visible: showError
        }

    }

    Timer{
        id:showTimer
        interval: 2000
        repeat:false
        onTriggered: {
            showError=false
        }
    }



}
