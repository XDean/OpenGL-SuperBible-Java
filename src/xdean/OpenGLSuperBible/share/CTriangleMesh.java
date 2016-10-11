package xdean.OpenGLSuperBible.share;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

public class CTriangleMesh {

	short[] pIndexes;
	float[][] pVerts;
	float[][] pNorms;
	float[][] pTexCoords;
	int nMaxIndexes;
	int nNumIndexes;
	int nNumVerts;
	GL2 gl;
	private FloatBuffer vb;
	private FloatBuffer nb;
	private FloatBuffer tb;
	private ShortBuffer ib;

	public CTriangleMesh(GL2 gl) {
		this.gl = gl;
	}

	public void Scale(float fScaleValue) {
		for (int i = 0; i < nNumVerts; i++)
			Math3d.m3dScaleVector3(pVerts[i], fScaleValue);
	}

	public void Draw() {
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vb);
		gl.glNormalPointer(GL2.GL_FLOAT, 0, nb);
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, tb);
		gl.glDrawElements(GL2.GL_TRIANGLES, nNumIndexes, GL2.GL_UNSIGNED_SHORT,
				ib);
	}

	private FloatBuffer wrapFloat2D(float[][] fss) {
		FloatBuffer buffer = Buffers.newDirectFloatBuffer(fss.length
				* fss[0].length);
		for (float[] fs : fss)
			buffer.put(fs);
		buffer.rewind();
		return buffer;
	}

	public void BeginMesh(int nMaxVerts) {
		nMaxIndexes = nMaxVerts;
		nNumIndexes = 0;
		nNumVerts = 0;

		pIndexes = new short[nMaxIndexes];
		pVerts = new float[nMaxIndexes][3];
		pNorms = new float[nMaxIndexes][3];
		pTexCoords = new float[nMaxIndexes][2];
	}

	public void AddTriangle(float verts[][]/* 3f3 */, float vNorms[][]/* 3f3 */,
			float vTexCoords[][]/* 2f3 */) {
		final float e = 0.000001f;
		Math3d.m3dNormalizeVector(vNorms[0]);
		Math3d.m3dNormalizeVector(vNorms[1]);
		Math3d.m3dNormalizeVector(vNorms[2]);

		for (int iVertex = 0; iVertex < 3; iVertex++) {
			int iMatch = 0;
			for (iMatch = 0; iMatch < nNumVerts; iMatch++) {
				if (Math3d.m3dCloseEnough(pVerts[iMatch][0], verts[iVertex][0],
						e)
						&& Math3d.m3dCloseEnough(pVerts[iMatch][1],
								verts[iVertex][1], e)
						&& Math3d.m3dCloseEnough(pVerts[iMatch][2],
								verts[iVertex][2], e)
						&&

						Math3d.m3dCloseEnough(pNorms[iMatch][0],
								vNorms[iVertex][0], e)
						&& Math3d.m3dCloseEnough(pNorms[iMatch][1],
								vNorms[iVertex][1], e)
						&& Math3d.m3dCloseEnough(pNorms[iMatch][2],
								vNorms[iVertex][2], e)
						&&

						Math3d.m3dCloseEnough(pTexCoords[iMatch][0],
								vTexCoords[iVertex][0], e)
						&& Math3d.m3dCloseEnough(pTexCoords[iMatch][1],
								vTexCoords[iVertex][1], e)) {
					pIndexes[nNumIndexes] = (short) iMatch;
					nNumIndexes++;
					break;
				}
			}

			if (iMatch == nNumVerts) {
				pVerts[nNumVerts] =Arrays.copyOf(verts[iVertex], 3);
				pNorms[nNumVerts] = Arrays.copyOf(vNorms[iVertex], 3);
				pTexCoords[nNumVerts] = Arrays.copyOf(vTexCoords[iVertex], 2);
				pIndexes[nNumIndexes] = (short) nNumVerts;
				nNumIndexes++;
				nNumVerts++;
			}
		}
	}

	public void EndMesh() {
		// GLushort *pPackedIndexes = new GLushort[nNumIndexes];
		// M3DVector3f *pPackedVerts = new M3DVector3f[nNumVerts];
		// M3DVector3f *pPackedNorms = new M3DVector3f[nNumVerts];
		// M3DVector2f *pPackedTex = new M3DVector2f[nNumVerts];
		//
		// memcpy(pPackedIndexes, pIndexes, sizeof(GLushort)*nNumIndexes);
		// memcpy(pPackedVerts, pVerts, sizeof(M3DVector3f)*nNumVerts);
		// memcpy(pPackedNorms, pNorms, sizeof(M3DVector3f)*nNumVerts);
		// memcpy(pPackedTex, pTexCoords, sizeof(M3DVector2f)*nNumVerts);
		//
		// delete [] pIndexes;
		// delete [] pVerts;
		// delete [] pNorms;
		// delete [] pTexCoords;
		//
		// pIndexes = pPackedIndexes;
		// pVerts = pPackedVerts;
		// pNorms = pPackedNorms;
		// pTexCoords = pPackedTex;
		pIndexes = Arrays.copyOf(pIndexes, nNumIndexes);
		pVerts = Arrays.copyOf(pVerts, nNumVerts);
		pNorms = Arrays.copyOf(pNorms, nNumVerts);
		pTexCoords = Arrays.copyOf(pTexCoords, nNumVerts);
		
		vb = wrapFloat2D(pVerts);
		nb = wrapFloat2D(pNorms);
		tb = wrapFloat2D(pTexCoords);
		ib = Buffers.newDirectShortBuffer(pIndexes);
	}
}
