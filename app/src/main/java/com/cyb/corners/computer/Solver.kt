package com.cyb.corners.computer

import com.cyb.corners.game.Board
import com.cyb.corners.game.Piece
import com.cyb.corners.player.Player

class Solver {
    companion object {
        private fun opponentTurn(board: Board,opponent: Player,moves: IntArray):IntArray{
            for(move in moves){
                for ((i,v) in opponent.pieces.withIndex()){
                    if(v){
                        return intArrayOf(0)
                    }
                }
            }
            return intArrayOf(0)
        }
        fun takeTurn(board: Board, moves:IntArray, opponent: Player, self:Player, depth:Int):IntArray{
            if(depth == 0)
                return moves
            val nextMove=Array<Pair<Int,Int>>(moves.size*4){Pair(-1,-1)}
            var count = 0
            for(move in moves){
                for((i,v) in self.pieces.withIndex()){
                    if(v) {
                        //copy the board and make place 'move' with orientation 'i'
                        val boardcpy = board.getCopy()
                        boardcpy.boolBoard[move] = false
                        boardcpy.lastPiece = Piece(move/board.size, move%board.size, i)
                        //now the piece is 'placed' get the opponent moves
                        val opponentMoves = Checker.getMoves(boardcpy, opponent)
                        //if opponent has no moves, is a winning move, play it
                        if(opponentMoves.isEmpty() && Checker.isValid(move,i, board.size))
                            return intArrayOf(move,i)
                        //opponent has moves to play
                        else{
                            if(takeTurn(boardcpy,opponentMoves,self,opponent, depth-1).isNotEmpty())
                                nextMove[count++]=Pair(move,i)
                        }
                    }
                }
            }
            if(count == 0)
                return IntArray(0){-1}
            val rnd = (0 until count).random()
            return intArrayOf(nextMove[rnd].first, nextMove[rnd].second)

        }
    }
}