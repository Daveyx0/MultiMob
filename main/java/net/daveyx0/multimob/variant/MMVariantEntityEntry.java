package net.daveyx0.multimob.variant;

public class MMVariantEntityEntry
{
	int variantIndex;
	String variantName;
	
	public MMVariantEntityEntry(int variantId, String name)
	{
		this.variantIndex = variantId;
		this.variantName = name;
	}

	public int getVariantIndex()
	{
		return variantIndex;
	}
	
	public String getVariantName()
	{
		return variantName;
	}
}
