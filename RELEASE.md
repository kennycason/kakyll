# Releasing


## Release to Nexus

Kakyll uses the `maven release` and `nexus-staging-maven-plugin` plugins to deploy to nexus.

The commands require GPG credentials to sign the jars.
Currently only the owner @kennycason can perform this.

The deploy command:

`mvn clean deploy -P release`

## Update Brew Install Scripts + Doc

Brew install script must contain the updated version and md5 checksums of the file.

Brew formula in GitHub can be found [here](https://raw.githubusercontent.com/kennycason/kakyll/master/script/brew/kakyll.rb)

md5 checksum can be calculated via:
`curl -sL https://search.maven.org/remotecontent\?filepath\=com/kennycason/kakyll-/1.5/kakyll-1.5.jar | shasum -a 256`

Additionally the README must be updated.%