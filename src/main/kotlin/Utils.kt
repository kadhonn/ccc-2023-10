import org.apache.commons.io.FileUtils
import java.io.File

class Utils {

    companion object {

    fun testFile(inputFile: File) {
        val outputFile = File(inputFile.absolutePath.replace(".in", ".out"))
        if (outputFile.exists()) {
            outputFile.delete()
        }

        val checkFile = File(inputFile.absolutePath.replace(".in", ".check"))

        val existingCheckFile = if (checkFile.exists()) {
            checkFile
        } else {
            null
        }
        computeAndCheck(inputFile, outputFile, existingCheckFile)
    }

    fun computeAndCheck(inputFile: File, outputFile: File, checkFile: File?) {
        if (FileUtils.sizeOf(inputFile) == 0L) {
            throw RuntimeException("Empty Input for ${inputFile.name}")
        }
        compute(inputFile, outputFile)
        if (FileUtils.sizeOf(outputFile) == 0L) {
            throw RuntimeException("Empty Solution for ${inputFile.name}")
        }
        else if (checkFile == null) {
            //assertTrue("Unchecked Solution for ${inputFile.name}", false)
        } else if (!FileUtils.contentEquals(outputFile, checkFile)) {
            throw RuntimeException("Wrong Solution for ${inputFile.name}");
        }
        else {
            println("Correct Solution for ${inputFile.name}")
        }
    }

        @JvmStatic
        fun fileForExample(example: String): File {
            return File("levels\\${CURRENT_LEVEL}\\${CURRENT_LEVEL}_$example.in")
        }

        @JvmStatic
        fun filesForExamples(examples: List<String>): List<File> {
            return examples.map { fileForExample(it) }
        }

        @JvmStatic
        fun someInputFiles(): List<File> {
            return filesForExamples(listOf("1", "2", "3"))
        }

        @JvmStatic
        fun allInputFiles(): List<File> {
            val levelDir = "levels\\${CURRENT_LEVEL}"
            return File(levelDir).walk().filter { it.isFile && it.absolutePath.endsWith(".in") }.toList()
        }
    }
}