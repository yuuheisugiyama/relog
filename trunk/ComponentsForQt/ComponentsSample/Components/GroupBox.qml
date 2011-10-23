import QtQuick 1.0

Item {
    width: parent.width
    height: parent.height

    property alias text: _text.text                     // 表示するメッセージ
    property alias color: _text.color                   // 色
    property alias fontPointSize: _text.font.pointSize  // フォントサイズ
    property alias textHeight: _text.height             // テキストの高さ
    property alias textBkColor: _textArea.color         // テキストの背景色

    property alias radius: _border.radius               // 角
    property color borderColor: "#999999"               // 枠の色
    property int borderWidth: 1                         // 枠の太さ

    // 枠線
    Rectangle {
        id: _border
        x: 0
        y: _text.height / 2
        width: parent.width
        height: parent.height - y

        border.color: borderColor
        border.width: borderWidth
    }

    // タイトル
    Rectangle {
        id: _textArea
        width: _text.width
        height: _text.height
        anchors.top: parent.top
        anchors.left: parent.left
        anchors.leftMargin: 5
        color: "#ffffff"

        Text{
            id: _text
            text: "Title"
        }
    }
}
