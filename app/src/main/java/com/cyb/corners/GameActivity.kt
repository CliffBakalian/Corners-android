package com.cyb.corners

import android.graphics.Color
import com.cyb.corners.game.*
import com.cyb.corners.player.Player
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.cyb.corners.computer.Checker
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {
    private lateinit var board: Board
    private lateinit var players:Array<Player>
    private var turnIndex:Int = 0
    private lateinit var currPiece:Piece

    private fun initGame(){
        board = Board(5)
        players = arrayOf(Player("Player One"), Player("Player Two"))
        reset()
    }
    private fun reset(){
        currPiece = Piece(0,0,5)
    }

    private fun refreshHud(){
        val currPieces = players[((turnIndex)%2)].pieces
        for((i,v) in currPieces.withIndex()){
            val tile:ImageView=findViewById(resources.getIdentifier("piece$i", "id", packageName))
            if(!v)
                tile.setImageResource(R.drawable.ic_empty)
            else {
                tile.setImageResource(R.drawable.ic_piece)
                tile.rotation = (i * 90).toFloat()
            }
        }
        val otherPieces = players[((turnIndex+1)%2)].pieces
        for((i,v) in otherPieces.withIndex()){
            val tile:ImageView=findViewById(resources.getIdentifier("otherPiece$i", "id", packageName))
            if(!v)
               tile.setImageResource(R.drawable.ic_empty)
            else {
                tile.setImageResource(R.drawable.ic_piece)
                tile.rotation = (i * 90).toFloat()
            }
        }
        val lbl: TextView = findViewById(resources.getIdentifier("player2_label", "id", packageName))
        if (turnIndex%2 ==1)
            lbl.text = "Player 1"
        else
            lbl.text = "Player 2"
    }

    private fun rotateCurr(initialValue:Int){
        currPiece.orientation++
        if(currPiece.orientation>3)
            currPiece.orientation=0
        if(currPiece.orientation == initialValue)
            return
        if(players[turnIndex%2].pieces[currPiece.orientation])
            return
        rotateCurr(initialValue)
    }
    private fun setCurrImage(orientation:Int){
        currPiece.orientation = orientation
        val location = currPiece.row*board.size + currPiece.col
        val button:ImageButton=findViewById(resources.getIdentifier("button$location", "id", packageName))
        if(currPiece.orientation == 5) {
            if(board.boolBoard[location]) {
                button.rotation = 0.toFloat()
                button.setImageResource(R.drawable.ic_empty)
            }
        }else{
            button.rotation = (orientation*90).toFloat()
            button.setImageResource(R.drawable.ic_piece)
        }
    }

    private fun setCurrImage(){
        setCurrImage(currPiece.orientation)
    }

    private fun setCurr(row:Int, col:Int){
        if(board.boolBoard[row*board.size+col])
            setCurrImage(5)
        currPiece.row = row
        currPiece.col = col
        val pieces = players[((turnIndex)%2)].pieces
        for((index, value) in pieces.withIndex())
            if(value) {
                currPiece.orientation = index
                break
            }
    }
    private fun gameOver(){
        for(i in 0..3){
            val tile:ImageView=findViewById(resources.getIdentifier("otherPiece$i", "id", packageName))
            tile.setImageResource(R.drawable.ic_empty)
        }
        submitButton.text = "Replay?"
        player2_label.text = "exit"
        player2_label.setOnClickListener { finish() }
        submitButton.setOnClickListener { finish(); startActivity(intent)}
    }

    fun makeMove(v:View){
        val row = v.tag.toString().toInt()/board.size
        val col = v.tag.toString().toInt()%board.size
        if(board.boolBoard[row*board.size+col]) {
            if (row != currPiece.row || col != currPiece.col)
                setCurr(row, col)
            else
                rotateCurr(currPiece.orientation)
            setCurrImage()
        }
    }

    fun commitMove(v:View){
        if(Checker.checkValid(board,players[turnIndex%2],currPiece)) {
            board.boolBoard[currPiece.row * board.size + currPiece.col] = false
            val location = currPiece.row * board.size + currPiece.col
            val button: ImageButton =
                findViewById(resources.getIdentifier("button$location", "id", packageName))
            when (turnIndex % 2) {
                0 -> button.setBackgroundColor(getSharedPreferences("com.cyb.prefs",0).getInt("P1_COLOR",Color.WHITE))
                1 -> button.setBackgroundColor(getSharedPreferences("com.cyb.prefs",0).getInt("P2_COLOR",Color.WHITE))
            }
            if(turnIndex%2==0)
                messages.text = resources.getString(R.string.player_two_turn)
            else
                messages.text = resources.getString(R.string.player_one_turn)
            players[(turnIndex)%2].pieces[currPiece.orientation] = false
            Log.i("Turnindex:", ""+turnIndex)
            if((turnIndex%8==7 || turnIndex%8==6))
                players[(turnIndex)%2].reset()
            turnIndex++
            board.lastPiece = currPiece
            val moves = Checker.getMoves(board,players[turnIndex%2])
            if(moves.isEmpty()) {
                gameOver()
            }
            else {
                reset()
                refreshHud()
            }
        }else
            messages.text = resources.getString(R.string.invalid_move)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()
        initGame()
    }
}
