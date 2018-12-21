class Kakyll < Formula
  desc "Kakyll: Static Site Generator in Kotlin"
  homepage "https://github.com/kennycason/kakyll"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kakyll/1.7/kakyll-1.7.jar"
  sha256 "6b353366a449b421c0f42896f5283cd1d75a4f08bb76f0ff6b9bff22e47ca362"

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

