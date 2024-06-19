
import QtQuick 2.9
import QtQuick.Controls 2.2
import "."

Item {
    anchors.fill: parent

    property string errorMessage: ""
    property string infoMessage: ""
    property real messageHeight: msg.height
    property bool hasError: errorMessage != ""
    property bool hasInfo: infoMessage != ""

    function init()
    {
    }

    function close()
    {
        window.backHome()
    }

    Rectangle {
        id: msg
        anchors.top: parent.top
        anchors.left: parent.left
        anchors.right: parent.right
        height: CapnometerSettings.fieldHeight
        color: hasError ? CapnometerSettings.errorColor : CapnometerSettings.infoColor
        visible: hasError || hasInfo

        Text {
            id: error
            anchors.fill: parent
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            minimumPixelSize: 5
            font.pixelSize: CapnometerSettings.smallFontSize
            fontSizeMode: Text.Fit
            color: CapnometerSettings.textColor
            text: hasError ? errorMessage : infoMessage
        }
    }
}
