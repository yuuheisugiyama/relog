import QtQuick 1.0

Image {
    property bool loading: (status != Image.Ready) & visible

    Image {
        anchors.centerIn: parent
        source: "images/spinner.png"

        NumberAnimation on rotation {
            from: 0
            to: 360
            duration: 1500
            loops: Animation.Infinite
            running: loading
        }

        visible: loading
    }
}
