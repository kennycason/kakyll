class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.5/kakyll-1.5.jar"
  sha256 "c68010dfa7bfaf58d022c8a470de0e8628a768d130f8d0a47a0cd28b685b90bc"

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

