import React, { useState } from 'react'
import { Table } from './components/Table';
import { InputFile } from './components/InputFile';
import { ButtonFetch } from './components/ButtonFetch';
import { DropdownLanguage } from './components/DropdownLanguage';
import { ButtonDownloadAll } from './components/ButtonDownloadAll';
import SubtitlesModel from './model/SubtitlesModel';

const App = () => {
    const [lang, setLang] = useState<string>('en')
    const [files, setFiles] = useState<Array<File>>([])
    const [subs, setSubs] = useState<Array<SubtitlesModel>>([])

    const onFileInput = (input: Array<File>) => {
        setFiles(input)
        setSubs([])
    }

    const onLanguageChange = (language: string) => {
        setLang(language)
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
                    <DropdownLanguage onSelect={onLanguageChange}/>
                    <ButtonFetch files={files} lang={lang} onFetch={onFetch}/>
                </div>
            </div>

            {subs.length > 1 ? (
                <div className='flex-item'>
                    <ButtonDownloadAll subtitles={subs}/>
                </div>
            ) : null}

            {subs.length > 0 ? (
                <div className='flex-item'>
                    <Table subs={subs}/>
                </div>
            ) : null}

        </div>
    )

}

export default App