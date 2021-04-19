import prettyBytes from 'pretty-bytes'
import { RiDownloadFill } from 'react-icons/ri'
import React from "react";
import SubtitlesModel from "../model/SubtitlesModel";

interface IProps {
    subtitles: SubtitlesModel
}

export const Row: React.FC<IProps> = ({ subtitles}) => {
    return (
        <>
            <td>{subtitles.name}</td>
            <td>{subtitles.host}</td>
            <td>{prettyBytes(subtitles.size)}</td>
            <td>{subtitles.ext}</td>
            <td>
                <button id="download">
                    <RiDownloadFill onClick={
                        () => downloadTxtFile(subtitles.name, subtitles.ext, subtitles.data)
                    } />
                </button>
            </td>
        </>
    )
}

const u_atob = (ascii: string): Uint8Array => {
    return Uint8Array.from(atob(ascii), c => c.charCodeAt(0));
}

const downloadTxtFile = (filename: string, ext: string, data: string): void => {
    const element = document.createElement("a");
    const string = new TextDecoder("CP1250").decode(u_atob(data))
    const file = new Blob([string], {type: 'text/plain'});
    element.href = URL.createObjectURL(file);
    element.download = `${filename}.${ext}`;
    document.body.appendChild(element);
    element.click();
}