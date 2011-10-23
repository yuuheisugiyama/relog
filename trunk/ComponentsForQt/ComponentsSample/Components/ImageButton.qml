import QtQuick 1.0

MouseArea {
    id: _root
    width: parent.width
    height: parent.height

    property string imageIdle: "../images/btn_menu_i.png"
    property string imagePress: "../images/btn_menu_p.png"

    Image {
        id: _image
        source: imageIdle
        width: parent.width
        height: parent.height

//        onSourceSizeChanged: {
//            width = sourceSize.width;
//            heigh = sourceSize.height;
//        }
    }
    states: [
        State {
            name: "pressed"
            when: _root.pressed
            PropertyChanges { target: _image; source: imagePress }
        }
    ]

}
