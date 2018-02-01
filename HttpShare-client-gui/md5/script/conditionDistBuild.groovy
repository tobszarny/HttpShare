import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.runtime.InvokerHelper
import org.fusesource.jansi.AnsiConsole

import java.security.MessageDigest

class ConditionDistBuild extends Script {

  static String[] args

  static void main(String[] args) {
    ConditionDistBuild.args = args
    InvokerHelper.runScript(ConditionDistBuild, args)
  }

  def generateMD5(final file) {
    MessageDigest digest = MessageDigest.getInstance("MD5")
    file.withInputStream() { is ->
      byte[] buffer = new byte[8192]
      int read = 0
      while ((read = is.read(buffer)) > 0) {
        digest.update(buffer, 0, read)
      }
    }
    byte[] md5sum = digest.digest()
    BigInteger bigInt = new BigInteger(1, md5sum)
    bigInt.toString(16).padLeft(32, '0')
  }

  def generateMD5File(final targetDir, final file) {
    def path = file.getAbsolutePath()
    path = path.substring(path.indexOf("src") + 4)
    def newFile = new File(targetDir, path + ".md5")
    newFile.getParentFile().mkdirs()
    newFile.createNewFile()
    newFile.setWritable(true)
    newFile << generateMD5(file)
  }

  def processFilesRecursively(final basedir, final processor, final targetDir) {
    def list = []
    if (basedir.exists()) {
      basedir.eachFileRecurse(FileType.FILES) { file ->
        list << file
        if (processor) {
          processor(targetDir, file)
        }
      }
    }
    list
  }

  def deleteAll(final basedir) {
    if (basedir.exists()) {
      basedir.eachFile() { file ->
        if (file.isDirectory()) {
          file.deleteDir()
        } else {

          file.delete()
        }
      }
    }
  }

  def equalFileContent(final leftFile, final rightFile) {
    def outcome = FileUtils.contentEquals(leftFile, rightFile)
    printf('%2$s %1$s %3$s\n', [leftFile.getAbsolutePath(), outcome ? "==" : "!=", rightFile.getAbsolutePath()])
    return outcome
  }

  def equalDirsContent(final leftDir, final rightDir) {
    def outcome = true
    leftDir.eachFileRecurse(FileType.FILES) { leftFile ->
      def fileSubPath = leftFile.getAbsolutePath().substring(leftDir.getAbsolutePath().length())
      def rightFile = new File(rightDir, fileSubPath)
      if (!equalFileContent(leftFile, rightFile)) {
        outcome = false
        return outcome
      }

    }
    return outcome
  }

  def copyFilesRecursively(final from, final to) {
    FileUtils.copyDirectory(from, to, true)
  }

  def shouldRebuild(final sourcesFiles, final prevMD5Files, final currentMd5Files, final currentMD5Dir, final prevMD5Dir) {
    sourcesFiles.size != prevMD5Files.size || prevMD5Files.size != currentMd5Files.size || !equalDirsContent(currentMD5Dir, prevMD5Dir)
  }

  def inheritIO(final src, final dest) {
    new Thread(new Runnable() {
      void run() {
        try {
          Scanner sc = new Scanner(src, "UTF-8")
          while (sc.hasNextLine()) {
            dest.println(sc.nextLine())
          }
        } catch (Exception e) {
          e.printStackTrace()
          throw e
        }
      }
    }).start()
  }

  def run() {
    AnsiConsole.systemInstall()
    def cli = new CliBuilder(usage: 'ls')
    cli.a('display all files')
    cli.l('use a long listing format')
    cli.t('sort by modification time')
    def options = cli.parse(this.args)

    println options.arguments()

    assert options

    def basedir = "${project.basedir}"
    def srcDir = new File("${basedir}/src")
    def prevMD5Dir = new File("${basedir}/md5/src/previous")
    def currentMD5Dir = new File("${basedir}/md5/src/current")

    deleteAll(currentMD5Dir)

    def prevMD5Files = processFilesRecursively(prevMD5Dir, null, null)
    def sourceFiles = processFilesRecursively(srcDir, { targetDir, file -> generateMD5File(targetDir, file) }, currentMD5Dir)
    def currentMD5Files = processFilesRecursively(currentMD5Dir, null, null)

    println "${project.basedir}"
    if (shouldRebuild(sourceFiles, prevMD5Files, currentMD5Files, currentMD5Dir, prevMD5Dir)) {
      println "Rebuild"
      def NODEJS_HOME = System.getenv("NODEJS_HOME")

      def builder = new ProcessBuilder()
        .command("$NODEJS_HOME/node.exe", "$NODEJS_HOME/node_modules/npm/bin/npm-cli.js", 'run', 'build-prod', '--scripts-prepend-node-path=auto')
        .directory(new File(basedir))
      def proc = builder.start()
      inheritIO(proc.getInputStream(), System.out)
      inheritIO(proc.getErrorStream(), System.out)

      proc.waitFor()

      deleteAll(prevMD5Dir)
      copyFilesRecursively(currentMD5Dir, prevMD5Dir)

      println "Done"
      AnsiConsole.systemUninstall()
    } else {
      println "Nothing to do"
    }
  }

}
