package com.example.chatapp.ui.main.Sezar

class SezarPost(val mesaj:String?) {
    private var moveKey:Int=4
    fun toSezar(): String{
        var intChar:Int=0
        var newChar:String=""
        var char:Char
        var password:String=""
        var i: Int=0
        while(i< mesaj!!.length){
            char=mesaj[i]
            intChar=char.toInt()+4
            newChar=intChar.toChar().toString()
            password+=newChar
            i++
        }
        return password
    }
}
class SezarGet(val sezar:String?){
    fun toMessage():String?{
        var intChar:Int=0
        var newChar:String=""
        var char:Char
        var undoSezar:String=""
        var i: Int=0
        while(i< sezar!!.length){
            char=sezar[i]
            intChar=char.toInt()-4
            newChar=intChar.toChar().toString()
            undoSezar+=newChar
            i++
        }
        return undoSezar
    }
}