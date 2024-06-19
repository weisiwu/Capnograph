import QtQuick 2.9
import QtQuick.Controls 2.2

Item {
    height: parent.height
    width: parent.width

    property alias title: upsideText.text
    property alias value: bottomText.text
    property alias display: bottomText.visible

    Text{
        id:upsideText
        anchors.top:parent.top
        anchors.horizontalCenter: parent.horizontalCenter
        height: parent.height*0.25
        width: parent.width
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        font.pixelSize: CapnometerSettings.mediumFontSize
        color:CapnometerSettings.textColor

    }

    Text{
        id:bottomText
        anchors.top: upsideText.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.verticalCenter: parent.verticalCenter
        anchors.verticalCenterOffset: upsideText.height*0.5
        height: parent.height*0.75
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        font.pixelSize: CapnometerSettings.giganticFontSize*2
        color:CapnometerSettings.textColor
    }
}
