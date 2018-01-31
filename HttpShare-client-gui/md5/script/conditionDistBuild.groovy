import groovy.io.FileType

import java.security.MessageDigest

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
  print "path before "
  println path
  path = path.substring(path.indexOf("src") + 4)
  print "path after "
  println path

  def newFile = new File(targetDir, path + ".md5")
  newFile.getParentFile().mkdirs()
  newFile.createNewFile()
  newFile.setWritable(true)
  newFile << generateMD5(file)
}

def processFilesRecursively(final basedir, final processor, final targetDir) {
  def list = []
  basedir.eachFileRecurse(FileType.FILES) { file ->
    print "file "
    println file
    list << file
    if (processor) {
      processor(targetDir, file)
    }
  }
  list
}

def deleteAll(final basedir) {
  basedir.eachFile() { file ->
    if (file.isDirectory()) {
      file.deleteDir()
    } else {

      file.delete()
    }
  }
}

def equalFilesContent(final left, right) {

}

def shouldRebuild(final sourcesFiles, final prevMD5Files, final currentMd5Files) {
  sourcesFiles.size != prevMD5Files.size || prevMD5Files.size != currentMd5Files.size || !equalFilesContent(currentMd5Files, prevMD5Files)
}

def inheritIO(final src, final dest) {
  new Thread(new Runnable() {
    void run() {
      try {
        Scanner sc = new Scanner(src)
        while (sc.hasNextLine()) {
          dest.println(sc.nextLine())
        }
      } catch (Exception e){
        e.printStackTrace()
        throw e
      }
    }
  }).start()
}

def cli = new CliBuilder(usage: 'ls')
cli.a('display all files')
cli.l('use a long listing format')
cli.t('sort by modification time')
def options = cli.parse(args)

println options.arguments()

assert options

def srcDir = new File("C:/git-ws/prv/HttpShare/HttpShare-client-gui/src")
def prevMd5Dir = new File("C:/git-ws/prv/HttpShare/HttpShare-client-gui/md5/src/previous")
def currentMd5Dir = new File("C:/git-ws/prv/HttpShare/HttpShare-client-gui/md5/src/current")

deleteAll(currentMd5Dir)

def prevMD5Files = processFilesRecursively(prevMd5Dir, null, null)
def sourceFiles = processFilesRecursively(srcDir, { targetDir, file -> generateMD5File(targetDir, file) }, currentMd5Dir)
def currentMD5Files = processFilesRecursively(currentMd5Dir, null, null)

if (shouldRebuild(sourceFiles, prevMD5Files, currentMD5Files)) {
  println "Rebuild"
  def NODEJS_HOME = System.getenv("NODEJS_HOME")

  def builder = new ProcessBuilder()
    .command("$NODEJS_HOME/node.exe", "$NODEJS_HOME/node_modules/npm/bin/npm-cli.js", 'run', 'build-prod', '--scripts-prepend-node-path=auto')
//    .inheritIO()
    .directory(new File("C:/git-ws/prv/HttpShare/HttpShare-client-gui"))
  def proc = builder.start()
//  inheritIO(proc.getInputStream(), System.out)
//  inheritIO(proc.getErrorStream(), System.err)

  proc.waitFor()
  //def proc = "$NODEJS_HOME/node.exe $NODEJS_HOME/node_modules/npm/bin/npm-cli.js run build-prod --scripts-prepend-node-path=auto".execute()
  //proc.waitForProcessOutput(System.out, System.err)
  println "Done"
}
