import React from 'react';
import JSZip from 'jszip';
import SubtitlesModel from '../model/SubtitlesModel';
import { base64_to_string } from '../helpers/Hashes';
import { download } from '../helpers/Downloader';

interface IProps {
    subtitles: Array<SubtitlesModel>
}

const zipFiles = (subtitles: Array<SubtitlesModel>): Promise<Blob> => {
    const zip = new JSZip();
    for (const subs of subtitles) {
        if (subs.data) {
            const name = `${subs.name}.${subs.ext}`
            const decoded = base64_to_string(subs.data)
            const out = new Blob([decoded], {type: 'text/plain'});
            zip.file(name, out)
        }
    }

    return zip.generateAsync({type: 'blob'})
}

export const ButtonDownloadAll: React.FC<IProps> = ({subtitles}) => {
    return (
        <>
            <button className='button-download button-download-single'
                    onClick={async () => download('subtitles', await zipFiles(subtitles))}
            >
                download all
            </button>
        </>
    )
}