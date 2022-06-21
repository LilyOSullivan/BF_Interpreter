@file:OptIn(ExperimentalUnsignedTypes::class)

import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

var memory = UByteArray(30_000)
var currentPointer = 0

fun main() {
    val path = "./src/main/kotlin/app.bf"
    val file = File(path)
    try {
        file.bufferedReader().use { br ->
            var line: String
            while (br.readLine().also { line = it ?: "" } != null) {
                var i = 0
                while(i < line.length) {
                    val currentInstruction = line[i]
                    if(currentInstruction == '[') { // What if .. [+++ \n - ] .. This can be fixed by using br.ReadText
                        val loopEnd = line.indexOf(']',i).also { if(it == -1) exit() }
                        val currentCondition = line.substring(i+1,loopEnd)
                        i = loopEnd
                        while(memory[currentPointer] != 0.toUByte()) {
                            for(currentOperation:Char in currentCondition) {
                                processInstruction(currentOperation)
                            }
                        }
                        continue
                    }
                    processInstruction(currentInstruction)
                    i++
                }
            }
        }
    } catch (e: IOException) { // File not found
        exit()
    }
}

fun processInstruction(c:Char) {
        when(c) {
            ',' -> comma()
            '.' -> period()
            '>' -> leftMove()
            '<' -> rightMove()
            '+' -> incrementValue()
            '-' -> decrementValue()
        }
}

fun exit() {
    println("Segmentation fault (core dumped)")
    exitProcess(1)
}

fun comma() {
    val input = readln()
    try {
        memory[currentPointer] = input.toUByte()
    } catch(e:NumberFormatException) {
        exit()
    }
}

fun period() {
    print(Char(memory[currentPointer].toInt()))
}

fun leftMove() {
    currentPointer++
}

fun incrementValue(){
    memory[currentPointer]++
}

fun decrementValue(){
    memory[currentPointer]--
}

fun rightMove() {
    currentPointer--
}