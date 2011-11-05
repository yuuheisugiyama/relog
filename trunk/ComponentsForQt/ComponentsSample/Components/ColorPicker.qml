import QtQuick 1.0

Rectangle {
    id: _root
    width: 200
    height: 150

    // トータルの色
    property color value: Qt.rgba(_barRed.value
                                  , _barGreen.value
                                  , _barBlue.value
                                  , _barAlpha.value)
    // 個別の色
    property alias red: _barRed.value
    property alias green: _barGreen.value
    property alias blue: _barBlue.value
    property alias alpha: _barAlpha.value

    // バーの横幅（これは外から操作すべきじゃない）
    property real barWidth: width / _bar.children.length
                            - _bar.spacing * (_bar.children.length - 1) / _bar.children.length

    // バーの隙間
    property alias rowSpacing: _bar.spacing
    // バーの枠関係の情報
    property int borderWidth: 2
    property color borderColor: "#444444"
    property int borderRadius: 2

    // アルファ使うか
    property alias useAlpha: _barAlpha.visible

    Column{
        anchors.fill: parent
        spacing: 5

        // 色選択バー
        Row{
            id: _bar
            height: parent.height * 0.9
            spacing: 5

            // 赤のバー
            ColorPickerBar{
                id: _barRed
                width: barWidth
                border.width: borderWidth
                border.color: borderColor
                radius: borderRadius
                viewRedMin: 0.0
                viewRedMax: 1.0
                viewGreenMin: _barGreen.value
                viewGreenMax: _barGreen.value
                viewBlueMin: _barBlue.value
                viewBlueMax: _barBlue.value
                viewAlphaMin: _barAlpha.value
                viewAlphaMax: _barAlpha.value
            }

            // 緑のバー
            ColorPickerBar{
                id: _barGreen
                width: barWidth
                border.width: borderWidth
                border.color: borderColor
                radius: borderRadius
                viewRedMin: _barRed.value
                viewRedMax: _barRed.value
                viewGreenMin: 0.0
                viewGreenMax: 1.0
                viewBlueMin: _barBlue.value
                viewBlueMax: _barBlue.value
                viewAlphaMin: _barAlpha.value
                viewAlphaMax: _barAlpha.value
            }
            // 青のバー
            ColorPickerBar{
                id: _barBlue
                width: barWidth
                border.width: borderWidth
                border.color: borderColor
                radius: borderRadius
                viewRedMin: _barRed.value
                viewRedMax: _barRed.value
                viewGreenMin: _barGreen.value
                viewGreenMax: _barGreen.value
                viewBlueMin: 0.0
                viewBlueMax: 1.0
                viewAlphaMin: _barAlpha.value
                viewAlphaMax: _barAlpha.value
            }
            // アルファのバー
            ColorPickerBar{
                id: _barAlpha
                width: barWidth
                border.width: borderWidth
                border.color: borderColor
                radius: borderRadius
                viewRedMin: _barRed.value
                viewRedMax: _barRed.value
                viewGreenMin: _barGreen.value
                viewGreenMax: _barGreen.value
                viewBlueMin: _barBlue.value
                viewBlueMax: _barBlue.value
                viewAlphaMin: 0.0
                viewAlphaMax: 1.0

                value: 1.0
            }
        }

        // 色説明的なの
        Row{
            height: parent.height - _bar.height - parent.spacing
            spacing: _bar.spacing
            // red
            Rectangle{
                width: _barRed.width
                height: parent.height
                color: "#ff0000"
                border.width: _barRed.border.width
                border.color: _barRed.border.color
                radius: _barRed.radius
            }
            // green
            Rectangle{
                width: _barGreen.width
                height: parent.height
                color: "#00ff00"
                border.width: _barGreen.border.width
                border.color: _barGreen.border.color
                radius: _barGreen.radius
            }
            // blue
            Rectangle{
                width: _barBlue.width
                height: parent.height
                color: "#0000ff"
                border.width: _barBlue.border.width
                border.color: _barBlue.border.color
                radius: _barBlue.radius
            }
            // alpha
            Rectangle{
                width: _barAlpha.width
                height: parent.height
                color: "#ffffffff"
                border.width: _barAlpha.border.width
                border.color: _barAlpha.border.color
                radius: _barAlpha.radius
                visible: _barAlpha.visible
            }
        }
    }
}
