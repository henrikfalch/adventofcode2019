package no.finntech.facade


class Day8 {
    fun createImageLayers(input : List<Int>, width:Int, height:Int): List<ImageLayer> {
        val imageSize = width * height
        val layers = input.size / imageSize
        return (0 until layers).map { ImageLayer(input.subList(it*imageSize, (it*imageSize)+imageSize), width, height) }
    }

    fun exc1() : Int{
        val input = fileAsShortIntList("day8.txt")
        val imageLayers = createImageLayers(input, 25, 6)
        var fewest0Digits = Int.MAX_VALUE
        var indexForFewest = 0

        imageLayers.forEachIndexed { index, imageLayer ->
            val num0Vals = imageLayer.numDigits(0)
            if (num0Vals < fewest0Digits) {
                fewest0Digits = num0Vals
                indexForFewest = index
            }
        }

        val imageLayer = imageLayers[indexForFewest]
        return imageLayer.numDigits(1) * imageLayer.numDigits(2)
    }

    fun exc2() {
        val input = fileAsShortIntList("day8.txt")
        val width = 25
        val height = 6

        val outputImage = getOutputImage(input, width, height)

        outputImage.forEachIndexed { index, i ->
            if (i == 0) {
                print(" ")
            } else {
                print(i)
            }
            if (index%width == width-1) {
                print("\n")
            }
        }
    }

    fun getOutputImage(input: List<Int>, width: Int, height: Int): MutableList<Int> {
        val imageLayers = createImageLayers(input, width, height)

        val imageSize = width * height
        val outputImage = mutableListOf<Int>()

        (0 until imageSize).forEach { pixel ->
            outputImage.add(imageLayers.map { it.input[pixel] }.firstOrNull { it != 2 } ?: 2)
        }
        return outputImage
    }


}

class ImageLayer(val input: List<Int>, val width:Int, val height:Int) {
    fun numDigits(digit:Int): Int {
        return input.filter { it == digit }.count()
    }

}

fun main() {
    //println("Exc1: ${Day8().exc1()}")
    Day8().exc2()
}
