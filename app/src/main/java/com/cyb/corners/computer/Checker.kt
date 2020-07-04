package com.cyb.corners.computer
import android.util.Log
import com.cyb.corners.game.*
import com.cyb.corners.player.Player

class Checker {
    companion object {
        private fun down(board:Board, lp: Int, piece: Int):Boolean{
            val rowStart = lp+board.size;
            val rowEnd = board.size*board.size
            for(i in rowStart until rowEnd step board.size){
                if(i == piece)
                    return true
            }
            return false
        }
        private fun right(board:Board, lp:Int, piece:Int):Boolean{
            val colEnd = ((lp/board.size)+1)*board.size
            for(i in lp+1 until colEnd){
                if(i == piece)
                    return true
            }
            return false
        }
        private fun left(board:Board, lp:Int, piece:Int):Boolean{
            val colStart = lp-(lp%board.size)
            for(i in colStart until lp){
                if(i == piece)
                    return true
            }
            return false
        }
        private fun up(board:Board, lp:Int, piece:Int):Boolean{
            val rowStart = lp%board.size;
            for(i in rowStart until lp step board.size){
                if(i == piece)
                    return true
            }
            return false
        }

        fun checkValid(board:Board, player:Player, piece:Piece):Boolean {
            val location = piece.row*board.size + piece.col
            val lpLocation = board.lastPiece.row*board.size + board.lastPiece.col
            if(piece.orientation == 5) return false
            Log.i("Orientatin: ",""+piece.orientation)
            Log.i("location: ",""+location)
            if(piece.orientation == 0 && location == ((board.size*board.size)-1)||
                piece.orientation == 1 && location == (board.size*(board.size-1))||
                piece.orientation == 2 && location == 0||
                piece.orientation == 3 && location == board.size-1)
                return false

            if(player.pieces[piece.orientation] && board.boolBoard[location]){
                return when(board.lastPiece.orientation){
                    0 -> down(board, lpLocation, location)||right(board, lpLocation, location)
                    1 -> down(board, lpLocation, location)||left(board, lpLocation, location)
                    2-> up(board, lpLocation, location)||left(board, lpLocation, location)
                    3 -> up(board, lpLocation, location)||right(board, lpLocation, location)
                    5 -> (location == (board.size*board.size)/2)
                    else -> {error("Invalid index for checking valid"+board.lastPiece.orientation)}
                }
            }
            return false
        }

        private fun down(board:Board, lp: Int):IntArray{
            val rowStart = lp+board.size;
            val rowEnd = board.size*board.size
            val arr = IntArray(board.size -lp/board.size - 1){_->-1}
            for(i in rowStart until rowEnd step board.size){
                if(board.boolBoard[i])
                    arr[i/board.size-lp/board.size-1] = i
            }
            return arr
        }
        private fun right(board:Board, lp:Int):IntArray{
            val colEnd = ((lp/board.size)+1)*board.size
            val arr = IntArray(colEnd - lp-1){_->-1}
            for(i in lp+1 until colEnd){
                if(board.boolBoard[i])
                    arr[i-lp-1]=i
            }
            return arr
        }
        private fun left(board:Board, lp:Int):IntArray{
            val colStart = lp-(lp%board.size)
            val arr = IntArray(lp-colStart){_->-1}
            for(i in colStart until lp){
                if(board.boolBoard[i])
                    arr[i%board.size]=i
            }
            return arr
        }
        private fun up(board:Board, lp:Int):IntArray{
            val rowStart = lp%board.size;
            val arr = IntArray((lp-rowStart)/board.size){_->-1}
            for(i in rowStart until lp step board.size){
                if(board.boolBoard[i])
                    arr[i/board.size] = i
            }
            return arr
        }
        fun getMoves(board:Board,player:Player):IntArray {
            val lpLocation = board.lastPiece.row * board.size + board.lastPiece.col
            var possible = when (board.lastPiece.orientation) {
                0 -> down(board, lpLocation) + right(board, lpLocation)
                1 -> down(board, lpLocation) + left(board, lpLocation)
                2 -> up(board, lpLocation) + left(board, lpLocation)
                3 -> up(board, lpLocation) + right(board, lpLocation)
                else -> {
                    error("Invalid index for checking valid" + board.lastPiece.orientation)
                }
            }
            var count = 0
            for (i in possible){
                if (i != -1)
                    count++
            }
            val temp = IntArray(count){_->-1}
            count = 0
            for ((index, value) in possible.withIndex()){
                if(value != -1){
                    temp[count] = value
                    count++
                }
            }
            possible = temp
            if (possible contentEquals intArrayOf(0)){
                if(player.pieces contentEquals booleanArrayOf(true, false, false, false))
                    possible = IntArray(0){_->-1}
            }else if(possible contentEquals intArrayOf(board.size)){
                if(player.pieces contentEquals booleanArrayOf(false, true, false, false))
                    possible = IntArray(0){_->-1}
            }else if(possible contentEquals intArrayOf(board.size*(board.size-1))) {
                if(player.pieces contentEquals booleanArrayOf(false, false, true, false))
                    possible = IntArray(0){_->-1}
            }else if(possible contentEquals intArrayOf(board.size*board.size-1)) {
                if(player.pieces contentEquals booleanArrayOf(false, false, false, true))
                    possible = IntArray(0){_->-1}
            }
            return possible
        }
        fun isDone(board: Board, player: Player):Boolean{
            return getMoves(board,player).isEmpty()

        }
    }
}