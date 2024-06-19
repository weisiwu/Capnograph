import QtQuick 2.0
import QtQuick.Controls 2.3

Item {
    height: CapnometerSettings.fieldHeight*2
    width: parent.width

    property alias title: title.text
    property alias value: slider.value
    property alias from:slider.from
    property alias to:slider.to
    property alias stepSize:slider.stepSize

    signal sliderMoved()

    Text{
        id:title
        anchors.top:parent.top
        height: CapnometerSettings.fieldHeight
        color:CapnometerSettings.textColor
        font.pixelSize: CapnometerSettings.smallFontSize
    }
    Text {
        id:disValue
        anchors.left: parent.left
        anchors.top:title.bottom
        height: CapnometerSettings.fieldHeight
        horizontalAlignment: Text.AlignVCenter
        verticalAlignment: Text.AlignVCenter
        color: CapnometerSettings.textColor
        font.pixelSize: CapnometerSettings.mediumFontSize
        text:slider.value
    }
    Slider{
        id:slider
        width: parent.width*0.8
        height: CapnometerSettings.fieldHeight
        anchors.top:title.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.horizontalCenterOffset: disValue.width*0.5

        onMoved: {
            sliderMoved();
        }
    }
}
