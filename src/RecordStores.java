import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import java.io.*;

class RecordStores
{
	private static String name="";
	private RecordStore rs=null;
	private int recordLength=0;

	public RecordStores (String name, int count)
	{
            this.name = name;
            recordLength=count*4;
		try {
			rs = null;
			rs = RecordStore.openRecordStore (name,true);
		}

		catch (RecordStoreFullException e) { rs=null; }

		catch (RecordStoreNotOpenException e) { rs=null; }

		catch (RecordStoreException e) { rs=null; }
	}

	public int getNumRecords ()
	{
		if (rs!=null) {
			try {
				return rs.getNumRecords ();
			}

			catch (RecordStoreNotOpenException e) { return 0; }

			catch (RecordStoreException e) { return 0; }
		}

		else return 0;
	}

	public int getRecord (int n)
	{
		int v=0;

		if (rs!=null) {
			try {
				byte[] arrData = rs.getRecord(n);
				ByteArrayInputStream bytes = new ByteArrayInputStream(arrData, 0, recordLength);
				DataInputStream dis = new DataInputStream(bytes);
				v=dis.readInt();
			}

			catch (IOException ioe) { return Integer.MIN_VALUE; }

			catch (RecordStoreException ex) { return Integer.MIN_VALUE; }

			return v;
		}

		else return Integer.MIN_VALUE;
	}

	public boolean setRecord (int n, int v)
	{
		if (rs!=null) {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bytes);

			try {
				dos.writeInt(v);
				rs.setRecord(n, bytes.toByteArray(), 0, bytes.toByteArray().length);
			}

			catch (IOException ioe) { return false; }

			catch (InvalidRecordIDException ridex) {
				try {
					rs.addRecord (bytes.toByteArray(), 0, bytes.toByteArray().length);
				}

				catch (RecordStoreException ex) { return false; }
			}

			catch (RecordStoreException ex) { return false; }

			return true;
		}

		else return false;
	}

	public boolean closeRecords()
	{
		try {
			rs.closeRecordStore();
			rs=null;
		}

		catch (RecordStoreException ex) { return false; }

		return true;
	}

}
