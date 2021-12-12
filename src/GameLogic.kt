class GameLogic(val boardsize: Int) {

    var backupBoard = arrayOf<Array<Int>>()
    var currentBoard = arrayOf<Array<Int>>()
    val ALIVE = 1
    val DEAD = 0

    init {
        (1..boardsize).forEach { i ->
            var row = arrayOf<Int>()
            (1..boardsize).forEach { j ->
                row += 0
            }
            currentBoard += row
        }
    }

    private fun neighbourCounter(x: Int, y: Int): Int {

        fun checkForInvalidSquare(x: Int, y: Int): Int {
            return if (x < 0 || x >= this.boardsize || y < 0 || y >= this.boardsize) 0 else currentBoard[x][y]

        }

        return checkForInvalidSquare(x - 1, y - 1) + checkForInvalidSquare(x, y - 1) + checkForInvalidSquare(
            x + 1,
            y - 1
        ) + checkForInvalidSquare(x - 1, y) + checkForInvalidSquare(x + 1, y) + checkForInvalidSquare(
            x - 1,
            y + 1
        ) + checkForInvalidSquare(x, y + 1) + checkForInvalidSquare(x + 1, y + 1)
    }

    fun step(){
        val nextstepBoard = Array(boardsize){Array<Int>(boardsize){0} }
        (0 until boardsize).forEach { i ->
            (0 until boardsize).forEach { j ->
                when (neighbourCounter(i,j)){
                    2 -> if (currentBoard[i][j] == DEAD) nextstepBoard[i][j] = DEAD else nextstepBoard[i][j] = ALIVE
                    3 -> nextstepBoard[i][j] = ALIVE
                    else -> nextstepBoard[i][j] = DEAD
                }
            }
        }
        currentBoard = nextstepBoard
    }

}