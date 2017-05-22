class Call < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyyll/1.0.0/kakyll-1.0.0.jar"
  sha256 "TODO"

  def install
    libexec.install "kakyll-1.0.0.jar"
    bin.write_jar_script libexec/"kakyll-1.0.0.jar", "call"
    puts "Finished installing Kakyll 1.0.0"
  end

  def server_script(server_jar); <<-EOS.undent
    #!/bin/bash
    exec java -cp #{server_jar} com.kennycason.kakyll.KakyllKt "$@"
  EOS
  end

  test do
    pipe_output("#{bin}/kakyll version", "Test Kakyll version command")
  end
end
