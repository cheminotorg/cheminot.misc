package org.cheminot.misc

import rapture.fs.FsUrl

object File {

  def currentDir: FsUrl = {
    val currentDir = new java.io.File("")
    rapture.fs.File.parse(currentDir.getAbsolutePath)
  }
}
