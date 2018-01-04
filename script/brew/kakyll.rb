class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.1/kakyll-1.1.jar"
  sha256 "599ae86eb73cd88aca79e126735eaf297dc826bb3e67aa5d0e9cd53d90b64cab"

  bottle :unneeded

  depends_on :java => "1.8+"

  def install
    libexec.install "kakyll-#{version}.jar"
    bin.write_jar_script libexec/"kakyll-#{version}.jar", "kakyll"
  end

  test do
    system "cd #{testpath}"
    system bin/"kakyll", "new", "my_site"
    assert_predicate testpath/"my_site", :exist?, "Site failed to generate"
  end
end

