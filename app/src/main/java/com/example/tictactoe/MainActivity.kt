package com.example.tictactoe

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var playerOneTurn = true
    private var playerOneMoves = mutableListOf<Int>()
    private var playerTwoMoves = mutableListOf<Int>()

    private val possibleWins: Array<List<Int>> = arrayOf(
        //Horizontal positions
        listOf(1,2,3),
        listOf(4,5,6),
        listOf(7,8,9),

        //Vertical Positions
        listOf(1,4,7),
        listOf(2,5,8),
        listOf(3,6,9),

        //Diagonal Positions
        listOf(1,5,9),
        listOf(3,5,7)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBoard()

        val reset: Button = findViewById(R.id.reset)
        reset.setOnClickListener{
            for (i in 1..9) {
                val button: Button = findViewById(i)
                button.text = ""
                button.isEnabled = true
                playerOneMoves.clear()
                playerTwoMoves.clear()
                playerOneTurn = true
                togglePlayerTurn(findViewById(R.id.player1), findViewById(R.id.player2))
            }
        }
    }

    private fun setupBoard() {
        var counter = 1

        val params1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        )

        val params2 = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        val board = findViewById<LinearLayout>(R.id.board)

        for (i in 1..3) {
            val linearlayout = LinearLayout(this)
            linearlayout.orientation = LinearLayout.HORIZONTAL
            linearlayout.layoutParams = params1
            params1.weight = 1.0F

            for (j in 1..3) {
                val button = Button(this)
                button.id = counter
                button.textSize = 42.0F
                button.setTextColor(
                    ContextCompat.getColor(this, R.color.colorAccent)
                )
                button.layoutParams = params2
                params2.weight = 1.0F
                button.setOnClickListener{
                    recordMove(it)
                    if (checkForDraw()) {
                        Toast.makeText(this, "Game Draw!", Toast.LENGTH_LONG).show()
                    }
                }
                linearlayout.addView(button)
                counter++
            }
            board.addView(linearlayout)
        }
    }

    private fun recordMove(view: View) {
        val button = view as Button
        val id = button.id

        if (playerOneTurn) {
            playerOneMoves.add(id)
            button.text = "O"
            button.isEnabled = false
            if (isWin(playerOneMoves)) {
                showInMessage(findViewById(R.id.player1Name), findViewById(R.id.player2Name))
            }
            else {
                playerOneTurn = false
                togglePlayerTurn(findViewById(R.id.player2), findViewById(R.id.player1))
            }
        }
        else {
            playerTwoMoves.add(id)
            button.text = "X"
            button.isEnabled = false
            if (isWin(playerTwoMoves)) {
                showInMessage(findViewById(R.id.player2Name), findViewById(R.id.player1Name))
            }
            else {
                playerOneTurn = true
                togglePlayerTurn(findViewById(R.id.player1), findViewById(R.id.player2))
            }
        }
    }

    private fun isWin(moves: List<Int>): Boolean {
        var won = false
        if (moves.size >= 3) {
            run loop@{
                possibleWins.forEach{
                    if (moves.containsAll(it)) {
                        won = true
                        return@loop
                    }
                }
            }
        }
        return (won)
    }

    private fun togglePlayerTurn(playerOn: TextView, playerOff: TextView) {
        playerOff.setTextColor(
            ContextCompat.getColor(this, R.color.colorPrimaryTextColor))
        playerOff.setTypeface(null, Typeface.NORMAL)
        playerOn.setTextColor(
            ContextCompat.getColor(this, R.color.colorAccent))
        playerOn.setTypeface(null, Typeface.BOLD)
    }

    private fun showInMessage(player: EditText, otherPlayer: EditText) {
        var playerName = player.text.toString()
        var otherPlayerName = otherPlayer.text.toString()
        if (playerName.isBlank()) {
            playerName = player.hint.toString()
        }
        if (otherPlayerName.isBlank()) {
            otherPlayerName = otherPlayer.hint.toString()
        }
        Toast.makeText(this, "Congratulations! $playerName has Won! SUCK IT $otherPlayerName XDXD",
            Toast.LENGTH_LONG).show()
        setAllButtonsOff()
    }

    private fun checkForDraw(): Boolean {
        if (allButtonsPressed()) {
            return true
        }
        return false
    }

    private fun allButtonsPressed(): Boolean {
        for (i in 1..9) {
            if (findViewById<Button>(i).isEnabled) {
                return false
            }
        }
        return true
    }

    private fun setAllButtonsOff() {
        for(i in 1..9) {
            val button: Button = findViewById(i)
            button.isEnabled = false
        }
    }
}