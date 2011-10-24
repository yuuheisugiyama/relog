import QtQuick 1.0

Rectangle{
    id: _root
//    width: parent.width
    height: (_textEdit.lineCount === undefined) ? (_textEdit.font.pixelSize + 10) : (_textEdit.font.pixelSize * _textEdit.lineCount + 10)
    color: "#dddddd"

    property alias text: _textEdit.text
    property alias fontPointSize: _textEdit.font.pointSize
    property alias textColor: _textEdit.color
    property alias textActiveFocus: _textEdit.activeFocus
    property alias focusEdit: _textEdit.focus
    property alias textCursorPosition: _textEdit.cursorPosition

    signal pressed(variant event)
    signal tabPressed(variant event)
    signal backtabPressed(variant event)
    signal enterPressed(variant event)
    signal escapePressed(variant event)


    // カーソルを最初に持っていく
    function moveCursorStart(){
        _textEdit.cursorPosition = 0;
    }

    // カーソルを最後に持っていく
    function moveCursorEnd(){
        _textEdit.cursorPosition = _textEdit.text.length;
    }

    onFocusChanged: {
        if(focus){
            // フォーカスが来たら実際のところへ更に移動
            _textEdit.forceActiveFocus();
        }
    }

    TextEdit {
        id: _textEdit
        x: 2
        y: 2
        width: parent.width - 2
        horizontalAlignment: TextEdit.AlignLeft
        verticalAlignment: TextEdit.AlignVCenter
        anchors.verticalCenter: parent.verticalCenter
        color: "#000000"
        wrapMode: TextEdit.WordWrap
        textFormat: TextEdit.PlainText
        font.pixelSize: 12
        focus: true

        Keys.onPressed: {
            pressed(event);
        }

        Keys.onTabPressed: {
            tabPressed(event);
        }
        Keys.onBacktabPressed: {
            backtabPressed(event);
        }
        Keys.onEnterPressed: {
            enterPressed(event);
        }
        Keys.onReturnPressed: {
            enterPressed(event);
        }
        Keys.onEscapePressed: {
            escapePressed(event);
        }
    }

    Rectangle{
        anchors.fill: parent
        anchors.rightMargin: 1
        anchors.bottomMargin: 1
        border.color: "#ffffff"
        color: "#00000000"
    }
    Rectangle{
        anchors.fill: parent
        anchors.topMargin: 1
        anchors.leftMargin: 1
        border.color: "#aaaaaa"
        color: "#00000000"
    }

}

