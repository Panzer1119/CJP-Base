function versionRegex (contents) {
    return RegExp('sonar.projectVersion=([\\d\\.]+)', 'm')
}

module.exports.readVersion = function (contents) {
    const matches = versionRegex(contents).exec(contents)
    if (matches === null) {
        throw new Error('Failed to read the version field in your sonar-project.properties file - is it present?')
    }
    return matches[1]
}

module.exports.writeVersion = function (contents, version) {
    return contents.replace(versionRegex(contents), () => {
        return `sonar.projectVersion=${version}`
    })
}