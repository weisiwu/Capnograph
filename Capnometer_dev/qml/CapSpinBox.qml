import QtQuick 2.0
import QtQuick.Controls 2.3

SpinBox {
    id: control
    editable: true
    wrap:true
    height: parent.height
    width: parent.width

    property int decimals:  1
    property real realValue: value / 10

    up.indicator: Rectangle {
        x: control.mirrored ? 0 : parent.width - width
        height: parent.height
        implicitWidth: 110
        implicitHeight: 80
        color: control.up.pressed ? "#e4e4e4" : "#f6f6f6"
        border.color: enabled ? "#21be2b" : "#bdbebf"

        Text {
            text: "+"
            font.pixelSize: control.font.pixelSize * 2
            color: "#21be2b"
            anchors.fill: parent
            fontSizeMode: Text.Fit
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
        }
    }

    down.indicator: Rectangle {
        x: control.mirrored ? parent.width - width : 0
        height: parent.height
        implicitWidth: 110
        implicitHeight: 80
        color: control.down.pressed ? "#e4e4e4" : "#f6f6f6"
        border.color: enabled ? "#21be2b" : "#bdbebf"

        Text {
            text: "-"
            font.pixelSize: control.font.pixelSize * 2
            color: "#21be2b"
            anchors.fill: parent
            fontSizeMode: Text.Fit
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
        }
    }

    validator: DoubleValidator {
        bottom: Math.min(from, to)
        top:  Math.max(from, to)
    }

    textFromValue: function(value, locale) {
        return Number(value / 10).toLocaleString(locale, 'f', control.decimals)
    }

    valueFromText: function(text, locale) {
        return Number.fromLocaleString(locale, text) * 10
    }
}


