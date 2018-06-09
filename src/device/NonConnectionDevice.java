package device;

import javax.naming.OperationNotSupportedException;

public class NonConnectionDevice implements ICommDevice {

	@Override
	public void open() throws Exception {
		throw new OperationNotSupportedException();
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public byte[] read() throws Exception {
		throw new OperationNotSupportedException();
	}

	@Override
	public void write(byte[] data) throws Exception {
		throw new OperationNotSupportedException();
	}

	@Override
	public void close() {
		return;
	}

	@Override
	public void addDeviceListener(Object listener) {
		return;
	}

	@Override
	public void removeDeviceListener(Object listener) {
		return;
	}

}
