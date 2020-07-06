package com.cyb.corners.game

import com.cyb.corners.computer.Checker

class Board (val size:Int){
    private var board = IntArray(size*size){_ -> 4}
    var boolBoard = BooleanArray(size*size){_ -> true}
    var lastPiece:Piece = Piece(0,0,5)

    public fun getRow(row:Int): IntArray {
        val array = IntArray(size);
        for (i in 0..size){
            if (i/size == row)
                array[i%size] = board[i]
        }
        return array
    }
    public fun getCol(row:Int): IntArray{
        val array = IntArray(size);
        for (i in 0..size){
            if (i%size == 0)
                array[i/size] = board[i]
        }
        return array
    }

    public fun getCell(row:Int, col:Int):Int{
        return board[row*size+col]
    }

    public fun setCell(row:Int, col:Int, value:Int){
        board[row*size+col] = value
        boolBoard[row*size+col] = false
    }
    fun getCopy():Board{
        val boardcpy = Board(size)
        for((i,v) in boolBoard.withIndex())
            boardcpy.boolBoard[i] = v
        for((i,v) in board.withIndex())
            boardcpy.board[i] = v
        boardcpy.lastPiece = lastPiece
        return boardcpy
    }
}