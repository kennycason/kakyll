class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.0/kakyll-1.0.jar"
  sha256 "665d27300c13c2dbb1a06b70eafe3bc7448756468bf60dfadf5ec7384902b973"

  def install
    libexec.install "kakyll-1.0.jar"
    bin.write_jar_script libexec/"kakyll-1.0.jar", "kakyll"
  end

  test do
    pipe_output("#{bin}/kakyll version", "Test Kakyll version command")
  end
end
