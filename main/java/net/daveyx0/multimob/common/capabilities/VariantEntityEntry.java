package net.daveyx0.multimob.common.capabilities;

public class VariantEntityEntry
{
	int variantIndex;
	String variantName;
	
	public VariantEntityEntry(int variantId, String name)
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
