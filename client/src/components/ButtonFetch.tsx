import React from "react"
import { RiFindReplaceLine } from 'react-icons/ri'
import SubtitlesModel from "../model/SubtitlesModel"
import { hash_napiprojekt, hash_opensubtitles_napisy24 } from "../helpers/Hashes";

interface IProps {
    files: Array<File>
    lang: string
    onFetch: (arg: Array<SubtitlesModel>) => void
}

const API_URL = process.env.REACT_APP_API_URL;

export const ButtonFetch: React.FC<IProps> = ({ files, lang, onFetch }) => {

    const fetchData = async(e: React.MouseEvent<HTMLButtonElement>) => {
        let target = e.currentTarget as HTMLButtonElement

        if (target.classList.contains('fetch-loading')) {
            return;
        }

        target.classList.toggle('fetch-loading')

        for (const file of files) {
            const hash_napi   = await hash_napiprojekt(file);
            const hash_os_n24 = await hash_opensubtitles_napisy24(file);

            const url = new URL(`${API_URL}/scan`)
            url.searchParams.append("opensubtitles.org", hash_os_n24!);
            url.searchParams.append("napiprojekt.pl", hash_napi!);
            url.searchParams.append("napisy24.pl", hash_os_n24!);
            url.searchParams.append("fs", String(file.size));
            url.searchParams.append("ln", lang);

            const res =  await fetch(url.toString()).then(res => res.json())

            onFetch([new SubtitlesModel(file.name, res.host, res.size, res.ext, res.data)])
        }

        target.classList.toggle('fetch-loading');
    }

    return (
        <>
            <button id="fetch"
                    onClick={(e) => fetchData(e)}>
                <span className="fetch-text">
                    <RiFindReplaceLine /> find
                </span>
            </button>
        </>
    )
}