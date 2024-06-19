import QtQuick 2.0

Rectangle {
    id:button
    color: CapnometerSettings.viewColor
    radius: CapnometerSettings.buttonRadius
    onEnabledChanged: checkColor()

    property color baseColor: CapnometerSettings.buttonColor
    property color pressedColor: "black"
   // property color disabledColor: CapnometerSettings.disabledButtonColor

    signal clicked()

    function checkColor()
    {
        if (!button.enabled) {
            //button.color = disabledColor
        } else {
            if (mouseArea.containsPress)
                button.color = pressedColor
            else
                button.color = baseColor
        }
    }

    MouseArea {
        id: mouseArea
        anchors.fill: parent
        onPressed: checkColor()
        onReleased: checkColor()
        onClicked: {
            checkColor()
            button.clicked()
        }
    }
}
