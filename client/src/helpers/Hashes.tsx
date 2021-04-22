import md5 from 'md5';
import Long from 'long';

export const u_atob = (ascii: string): Uint8Array => {
    return Uint8Array.from(atob(ascii), c => c.charCodeAt(0));
}

export const base64_to_string = (data: string) => {
    return new TextDecoder('CP1250')
        .decode(u_atob(data));
}

export const hash_napiprojekt = async (file: Blob): Promise<string | null> => {
    let size = Math.min(1024 * 1024 * 10, file.size);
    let bytes = await readBytes(file, 0, size);
    if (bytes == null) {
        return null;
    }
    return md5(new Uint8Array(bytes))
}

export const hash_opensubtitles_napisy24 = async (file: Blob): Promise<string | null> => {
    const chunkSize = 65536;
    const size = file.size;
    const offset = Math.max(size - chunkSize, 0);

    const head = await readBytes(file, 0, 65536);
    const tail = await readBytes(file, offset, file.size);

    if (head == null || tail == null) {
        return null;
    }

    const lh = computeHashForChunk(new DataView(head));
    const th = computeHashForChunk(new DataView(tail));
    const res = lh.add(th).add(Long.fromInt(size));
    return toHexString(res.toBytes())
}

const toHexString = (byteArray: number[]): string => {
    return Array.from(byteArray, function (byte) {
        return ('0' + (byte & 0xFF).toString(16)).slice(-2);
    }).join('')
}


const computeHashForChunk = (buffer: DataView): Long => {
    let hash = Long.fromInt(0);
    for (let i = 0; i < buffer.byteLength; i += 8) {
        let o = buffer.getBigInt64(i, true);
        hash = hash.add(Long.fromString(o.toString()))
    }
    return hash;
}

export const readBytes = (data: Blob, start: number, end: number): Promise<ArrayBuffer | null> => {
    let reader = new FileReader();
    data = data.slice(start, end)

    return new Promise((resolve, reject) => {
        reader.onerror = (e) => reject(e);
        // @ts-ignore
        reader.onload = () => resolve(reader.result);
        reader.readAsArrayBuffer(data);
    });
};
