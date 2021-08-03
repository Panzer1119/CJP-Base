function versionRegex (contents) {
    return RegExp('String VERSION = "([\\d\\.]+)";', 'm')
}

module.exports.readVersion = function (contents) {
    const matches = versionRegex(contents).exec(contents)
    if (matches === null) {
        throw new Error('Failed to read the version field in your main java file - is it present?')
    }
    return matches[1]
}

module.exports.writeVersion = function (contents, version) {
    return contents.replace(versionRegex(contents), () => {
        return `String VERSION = "${version}";`
    })
}