package net.daveyx0.multimob.common.capabilities;

import java.util.concurrent.Callable;

/**
 * @author Daveyx0
 **/
public class VariantEntityHandler implements IVariantEntity {

	protected int variantId;
	
	public VariantEntityHandler()
	{
		variantId = 0;
	}
	
	
	public VariantEntityHandler(int id)
	{
		variantId = id;
	}
	
	private static class Factory implements Callable<IVariantEntity> {

		  @Override
		  public IVariantEntity call() throws Exception {
		    return new VariantEntityHandler();
		  }
	}

	@Override
	public int getVariant() {
		return variantId;
	}


	@Override
	public void setVariant(int id) {
		variantId = id;
		
	}

}
