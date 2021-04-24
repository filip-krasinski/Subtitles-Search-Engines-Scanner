import React, { useState } from 'react'
import { Table } from './components/Table';
import { InputFile } from './components/InputFile';
import { ButtonFetch } from './components/ButtonFetch';
import { DropdownLanguage } from './components/DropdownLanguage';
import { ButtonDownloadAll } from './components/ButtonDownloadAll';
import { Pagination } from './components/Pagiantion';
import SubtitlesModel from './model/SubtitlesModel';

const resultPerPage = 10

const App = () => {
    const [lang, setLang] = useState<string>('en')
    const [files, setFiles] = useState<Array<File>>([])
    const [subs, setSubs] = useState<Array<SubtitlesModel>>([])
    const [page, setPage] = useState<number>(1)

    const onFileInput = (input: Array<File>) => {
        setFiles(input)
        setSubs([])
    }

    const onFetch = (fetched: Array<SubtitlesModel>) => {
        setSubs(arr => [...arr, ...fetched])
    }

    return (
        <div className='flex-container'>

            <div className='flex-item'>
                <InputFile onInput={onFileInput}/>
            </div>

            <div className='flex-item'>
                <div className='flex-row-item'>
                    <DropdownLanguage onSelect={setLang}/>
                    <ButtonFetch files={files} lang={lang} onFetch={onFetch}/>
                </div>
            </div>

            {subs.length > 1 ? (
                <div className='flex-item'>
                    <ButtonDownloadAll subtitles={subs}/>
                </div>
            ) : null}

            {Math.floor(subs.length / resultPerPage) + 1 > 1 ? (
                <div className='flex-item'>
                    <Pagination currentPage={page} subsPerPage={resultPerPage} totalSubs={subs.length} setPage={setPage}/>
                </div>
                ) : null
            }
            {subs.length > 0 ? (
                <div className='flex-item'>
                    <Table subs={subs} page={page} subsPerPage={resultPerPage}/>
                </div>
            ) : null}

        </div>
    )

}

export default App