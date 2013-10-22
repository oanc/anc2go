class Masc {
	static final File root = new File('/var/corpora/MASC-3.0.0')
	static final File header = new File(root, 'resource-header')
	static final File data = new File(root, 'data')
}

Masc.data.eachDirRecurse { println it.path }
