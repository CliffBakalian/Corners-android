package com.cyb.corners.player

class Player (var name:String){
    var pieces = BooleanArray(4){_->true}

    public fun reset(){
        pieces = BooleanArray(4){_->true}
    }

//    private fun PlayPiece(index:Int):Boolean{
//        if (pieces[index]) {
//            pieces[index] = false
//            return true
//        }
//        return false
//    }
}