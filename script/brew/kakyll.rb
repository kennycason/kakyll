class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.3/kakyll-1.3.jar"
  sha256 "714f20eeb7d8082fbb82c8e7ba26eb31403b91e91f8d9f5e2db76454f6ad8c68"

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

