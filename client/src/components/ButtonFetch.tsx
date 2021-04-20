import md5 from 'md5'
import Long from 'long'
import React from "react"
import { RiFindReplaceLine } from 'react-icons/ri'
import SubtitlesModel from "../model/SubtitlesModel"

interface IProps {
    files: Array<File>
    lang: string
    onFetch: (arg: Array<SubtitlesModel>) => void
}

export const ButtonFetch: React.FC<IProps> = ({ files, lang, onFetch }) => {

    const fetchData = async() => {
        for (let i = 0; i < files.length; ++i) {
            const file = files[i];
            const napi = await napiprojekt(file);
            const op_na = await opensubtitles_napisy24(file);
            const url = `http://localhost:8080/api/scan?opensubtitles.org=${op_na}&napisy24.pl=${op_na}&napiprojekt.pl=${napi}&fs=${file.size}&ln=${lang}`
            let res =  await fetch(url).then(res => res.json())

            let subs = []
            for (let j = 0; j < res.length; ++j) {
                subs.push(new SubtitlesModel(file.name, res[j].host, res[j].size, res[j].ext, res[j].data))
            }
            onFetch(subs)
        }
    }

    return (
        <>
            <button id="fetch" onClick={fetchData}>
                <RiFindReplaceLine /> find
            </button>
        </>
    )
}

const napiprojekt = async(file: Blob): Promise<string | null> => {
    let size = Math.min(1024 * 1024 * 10, file.size);
    let bytes = await readBytes(file, 0, size);
    if (bytes == null) {
        return null;
    }
    return md5(new Uint8Array(bytes))
}

const opensubtitles_napisy24 = async(file: Blob): Promise<string | null> => {
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
    return Array.from(byteArray, function(byte) {
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

const readBytes = (data: Blob, start: number, end: number): Promise<ArrayBuffer | null > => {
    let reader = new FileReader();
    data = data.slice(start, end)

    return new Promise((resolve, reject) => {
        reader.onerror = (e) => reject(e);
        // @ts-ignore
        reader.onload = () => resolve(reader.result);
        reader.readAsArrayBuffer(data);
    });
};