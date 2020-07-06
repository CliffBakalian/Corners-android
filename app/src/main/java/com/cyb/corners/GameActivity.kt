package com.cyb.corners

import android.content.SharedPreferences
import android.graphics.Color
import com.cyb.corners.game.*
import com.cyb.corners.player.Player
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.cyb.corners.computer.Checker
import com.cyb.corners.computer.Solver
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {
    private lateinit var board: Board
    private lateinit var players:Array<Player>
    private var turnIndex:Int = 0
    private lateinit var currPiece:Piece
    private var computer = false

    private fun initGame(){
        board = Board(5)
        players = arrayOf(Player("Player One"), Player("Player Two"))
        reset()
        val b = intent.extras
        computer = false
        if(b!=null)
            computer = b.getBoolean("AI")
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
        lbl.text = players[(turnIndex+1)%2].name
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
        if(computer) {
            val tg = getSharedPreferences("com.cyb.prefs",0).getInt("TOTAL_GAMES",0)
            val editor = getSharedPreferences("com.cyb.prefs",0).edit()
            editor.putInt("TOTAL_GAMES", tg + 1)
            if (turnIndex%2==1) {
                val tw = getSharedPreferences("com.cyb.prefs",0).getInt("TOTAL_WINS",0)
                editor.putInt("TOTAL_WINS", tw + 1)
            }
            editor.apply()
        }
        messages.text = players[((turnIndex-1)%2)].name +" WINS!"
        submitButton.text = resources.getString(R.string.replay_button)
        player2_label.text = resources.getString(R.string.end_button)
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
                0 -> button.setBackgroundColor(getSharedPreferences("com.cyb.prefs",0).getInt("P1_COLOR",Color.BLUE))
                1 -> button.setBackgroundColor(getSharedPreferences("com.cyb.prefs",0).getInt("P2_COLOR",Color.RED))
            }
            if(turnIndex%2==0)
                messages.text = resources.getString(R.string.player_two_turn)
            else
                messages.text = resources.getString(R.string.player_one_turn)
            players[(turnIndex)%2].pieces[currPiece.orientation] = false
            turnIndex++
            if((turnIndex%8==7 || turnIndex%8==0))
                players[(turnIndex+1) % 2].reset()

            board.lastPiece = currPiece
            val moves = Checker.getMoves(board,players[turnIndex%2])
            if(moves.isEmpty()) {
                gameOver()
            }
            else {
                if(computer){
                    val move = Solver.takeTurn(board,moves,players[0], players[1],1)
                    val position = move[0]
                    val orientation = move[1]
                    board.boolBoard[position] = false
                    setCurr(position/board.size,position%board.size)
                    setCurrImage(orientation)
                    val aiMove: ImageButton =
                        findViewById(resources.getIdentifier("button$position", "id", packageName))
                     aiMove.setBackgroundColor(getSharedPreferences("com.cyb.prefs",0).getInt("P2_COLOR",Color.WHITE))
                    players[1].pieces[orientation] = false
                    turnIndex++
                    if((turnIndex%8==7 || turnIndex%8==0))
                        players[(turnIndex+1) % 2].reset()
                    messages.text = resources.getString(R.string.player_one_turn)
                    if(Checker.getMoves(board,players[turnIndex%2]).isEmpty()) gameOver()

                }
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
