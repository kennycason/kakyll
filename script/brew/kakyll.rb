class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.1/kakyll-1.1.jar"
  sha256 "599ae86eb73cd88aca79e126735eaf297dc826bb3e67aa5d0e9cd53d90b64cab"

  def install
    libexec.install "kakyll-1.1.jar"
    bin.write_jar_script libexec/"kakyll-1.1.jar", "kakyll"
  end

  test do
    pipe_output("#{bin}/kakyll version", "Test Kakyll version command")
  end
end
