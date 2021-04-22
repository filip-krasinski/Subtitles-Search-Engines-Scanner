import React from 'react';
import prettyBytes from 'pretty-bytes'
import SubtitlesModel from '../model/SubtitlesModel';
import { base64_to_string } from '../helpers/Hashes';
import { download } from '../helpers/Downloader';
import { RiDownloadFill } from 'react-icons/ri'

interface IProps {
    subtitles: SubtitlesModel
}

export const Row: React.FC<IProps> = ({subtitles}) => {
    return (
        <>
            <td>{subtitles.name}</td>
            <td>{subtitles.host}</td>
            <td>{prettyBytes(subtitles.size)}</td>
            <td>{subtitles.ext}</td>
            <td>
                <button className={'button-download'}>
                    <RiDownloadFill onClick={
                        () => {
                            const name = `${subtitles.name}.${subtitles.ext}`
                            const decoded = base64_to_string(subtitles.data)
                            const out = new Blob([decoded], {type: 'text/plain'});
                            download(name, out);
                        }
                    }/>
                </button>
            </td>
        </>
    )
}